# vim: set fileencoding=utf-8:
#
# GPIO Zero: a library for controlling the Raspberry Pi's GPIO pins
#
# Copyright (c) 2015-2023 Dave Jones <dave@waveform.org.uk>
# Copyright (c) 2022 gnicki2000 <89583687+gnicki2000@users.noreply.github.com>
# Copyright (c) 2020 Fangchen Li <fangchen.li@outlook.com>
# Copyright (c) 2015-2020 Ben Nuttall <ben@bennuttall.com>
# Copyright (c) 2019 tuftii <3215045+tuftii@users.noreply.github.com>
# Copyright (c) 2019 tuftii <pi@raspberrypi>
# Copyright (c) 2019 Yisrael Dov Lebow 🐻 <lebow@lebowtech.com>
# Copyright (c) 2019 Kosovan Sofiia <sofiia.kosovan@gmail.com>
# Copyright (c) 2016-2019 Andrew Scheller <github@loowis.durge.org>
# Copyright (c) 2016 Ian Harcombe <ian.harcombe@gmail.com>
#
# SPDX-License-Identifier: BSD-3-Clause

import warnings
from threading import Lock
from itertools import repeat, cycle, chain

from collections import OrderedDict
from math import log2

from .exc import (
    OutputDeviceBadValue,
    GPIOPinMissing,
    PWMSoftwareFallback,
    DeviceClosed,
)
from .devices import GPIODevice, Device, CompositeDevice
from .mixins import SourceMixin
from .threads import GPIOThread

try:
    from .pigpio import PiGPIOFactory
except ImportError:
    PiGPIOFactory = None


class OutputDevice(SourceMixin, GPIODevice):
    """
    Represents a generic GPIO output device.

    This class extends :class:`GPIODevice` to add facilities common to GPIO
    output devices: an :meth:`on` method to switch the device on, a
    corresponding :meth:`off` method, and a :meth:`toggle` method.

    :type pin: int or str
    :param pin:
        The GPIO pin that the device is connected to. See :ref:`pin-numbering`
        for valid pin numbers. If this is :data:`None` a :exc:`GPIODeviceError`
        will be raised.

    :param bool active_high:
        If :data:`True` (the default), the :meth:`on` method will set the GPIO
        to HIGH. If :data:`False`, the :meth:`on` method will set the GPIO to
        LOW (the :meth:`off` method always does the opposite).

    :type initial_value: bool or None
    :param initial_value:
        If :data:`False` (the default), the device will be off initially.  If
        :data:`None`, the device will be left in whatever state the pin is
        found in when configured for output (warning: this can be on).  If
        :data:`True`, the device will be switched on initially.

    :type pin_factory: Factory or None
    :param pin_factory:
        See :doc:`api_pins` for more information (this is an advanced feature
        which most users can ignore).
    """
    def __init__(self, pin=None, *, active_high=True, initial_value=False,
                 pin_factory=None):
        super().__init__(pin, pin_factory=pin_factory)
        self._lock = Lock()
        self.active_high = active_high
        if initial_value is None:
            self.pin.function = 'output'
        else:
            self.pin.output_with_state(self._value_to_state(initial_value))

    def _value_to_state(self, value):
        return bool(self._active_state if value else self._inactive_state)

    def _write(self, value):
        try:
            self.pin.state = self._value_to_state(value)
        except AttributeError:
            self._check_open()
            raise

    def on(self):
        """
        Turns the device on.
        """
        self._write(True)

    def off(self):
        """
        Turns the device off.
        """
        self._write(False)

    def toggle(self):
        """
        Reverse the state of the device. If it's on, turn it off; if it's off,
        turn it on.
        """
        with self._lock:
            if self.is_active:
                self.off()
            else:
                self.on()

    @property
    def value(self):
        """
        Returns 1 if the device is currently active and 0 otherwise. Setting
        this property changes the state of the device.
        """
        return super().value

    @value.setter
    def value(self, value):
        self._write(value)

    @property
    def active_high(self):
        """
        When :data:`True`, the :attr:`value` property is :data:`True` when the
        device's :attr:`~GPIODevice.pin` is high. When :data:`False` the
        :attr:`value` property is :data:`True` when the device's pin is low
        (i.e. the value is inverted).

        This property can be set after construction; be warned that changing it
        will invert :attr:`value` (i.e. changing this property doesn't change
        the device's pin state - it just changes how that state is
        interpreted).
        """
        return self._active_state

    @active_high.setter
    def active_high(self, value):
        self._active_state = True if value else False
        self._inactive_state = False if value else True

    def __repr__(self):
        try:
            return (
                f'<gpiozero.{self.__class__.__name__} object on pin '
                f'{self.pin!r}, active_high={self.active_high}, '
                f'is_active={self.is_active}>')
        except:
            return super().__repr__()


class DigitalOutputDevice(OutputDevice):
    """
    Represents a generic output device with typical on/off behaviour.

    This class extends :class:`OutputDevice` with a :meth:`blink` method which
    uses an optional background thread to handle toggling the device state
    without further interaction.

    :type pin: int or str
    :param pin:
        The GPIO pin that the device is connected to. See :ref:`pin-numbering`
        for valid pin numbers. If this is :data:`None` a :exc:`GPIODeviceError`
        will be raised.

    :param bool active_high:
        If :data:`True` (the default), the :meth:`on` method will set the GPIO
        to HIGH. If :data:`False`, the :meth:`on` method will set the GPIO to
        LOW (the :meth:`off` method always does the opposite).

    :type initial_value: bool or None
    :param initial_value:
        If :data:`False` (the default), the device will be off initially.  If
        :data:`None`, the device will be left in whatever state the pin is
        found in when configured for output (warning: this can be on).  If
        :data:`True`, the device will be switched on initially.

    :type pin_factory: Factory or None
    :param pin_factory:
        See :doc:`api_pins` for more information (this is an advanced feature
        which most users can ignore).
    """
    def __init__(self, pin=None, *, active_high=True, initial_value=False,
                 pin_factory=None):
        self._blink_thread = None
        self._controller = None
        super().__init__(pin, active_high=active_high,
                         initial_value=initial_value, pin_factory=pin_factory)

    @property
    def value(self):
        return super().value

    @value.setter
    def value(self, value):
        self._stop_blink()
        self._write(value)

    def close(self):
        self._stop_blink()
        super().close()

    def on(self):
        self._stop_blink()
        self._write(True)

    def off(self):
        self._stop_blink()
        self._write(False)

    def blink(self, on_time=1, off_time=1, n=None, background=True):
        """
        Make the device turn on and off repeatedly.

        :param float on_time:
            Number of seconds on. Defaults to 1 second.

        :param float off_time:
            Number of seconds off. Defaults to 1 second.

        :type n: int or None
        :param n:
            Number of times to blink; :data:`None` (the default) means forever.

        :param bool background:
            If :data:`True` (the default), start a background thread to
            continue blinking and return immediately. If :data:`False`, only
            return when the blink is finished (warning: the default value of
            *n* will result in this method never returning).
        """
        self._stop_blink()
        self._blink_thread = GPIOThread(
            self._blink_device, (on_time, off_time, n))
        self._blink_thread.start()
        if not background:
            self._blink_thread.join()
            self._blink_thread = None

    def _stop_blink(self):
        if getattr(self, '_controller', None):
            self._controller._stop_blink(self)
        self._controller = None
        if getattr(self, '_blink_thread', None):
            self._blink_thread.stop()
        self._blink_thread = None

    def _blink_device(self, on_time, off_time, n):
        iterable = repeat(0) if n is None else repeat(0, n)
        for _ in iterable:
            self._write(True)
            if self._blink_thread.stopping.wait(on_time):
                break
            self._write(False)
            if self._blink_thread.stopping.wait(off_time):
                break



















