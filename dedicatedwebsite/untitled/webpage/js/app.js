function helloWorld() {
    console.log("Hello, world!");
    alert("Hello world!");
}


function logIn(username, password) {

    console.log("Log In button clicked");


}

function signUp(username, password, email) {

    console.log("Sign Up button clicked");
}


function logInToApplication(username, password) {

    if (username && password) {
        // Simulate successful login
        
        redirectToMainApplication();
    } else {
        alert("Please enter both username and password.");
    }

    console.log("Logging in to application...");
}


function redirectToMainApplication() {
    window.location.assign('appmainmenu.html');
}

function goToAIControl() {
    stopCar();
    window.location.assign('aicontrol.html');
}

function goToAlexaMode() {

    window.location.assign('alexamode.html');
}


function goToMainMenu() {

    stopCar();
    window.location.assign('appmainmenu.html');
}

function goToManualControl() {
    window.location.assign('movement.html');
}


let isCarOn = "Off";
let websocket = null;

window.onload = function() {
    document.querySelector('h1').innerHTML = isCarOn;

}




function startCar() {
    console.log("Car started");
    

    //this one will activate the car controls on the manual control menu
    //it will toggle them between colors for on and off
    //it will also send a message to the backend to put the car into manual control mode
    //this function activates a websocket connection too

    isCarOn = "On";
    document.querySelector('h1').innerHTML = isCarOn;
    websocket = new WebSocket('ws://your-websocket-url');
    websocket.onopen = function(event) {
        console.log('WebSocket connection opened');
    }

    websocket.onmessage = function(event) {
        console.log('WebSocket message received:', event.data);
    }
    websocket.onclose = function(event) {
        console.log('WebSocket connection closed');
    }



}


function stopCar() {
    console.log("Car stopped");
    
    //this one will deactivate the car controls on the manual control menu
    //it will toggle them between colors for on and off
    //it will also send a message to the backend to put the car into a neutral state

    isCarOn = "Off";

    document.querySelector('h1').innerHTML = isCarOn;
    //this function closes the websocket connection if it is active

    if (websocket) {
        websocket.close();
        websocket = null;
    }

}

function controlCar(event) {
    console.log("Controlling car");
    if (isCarOn === "On") {
        switch (event.key) {
            case "w":
                moveforward();
                break;
            case "s":
                movebackward();
                break;
            case "a":
                moveleft();
                break;
            case "d":
                moveright();
                break;


            case "i":
                turncameraup();
                break;
            case "k":
                turncameradown();
                break;
            case "j":
                turncameraleft();
                break;
            case "l":
                turncameraright();
                break;
        }
    }
}


function moveforward() {
    console.log("Moving forward");
    if (websocket) {
        websocket.send('forward');
    }
}
function movebackward() {
    console.log("Moving backward");
    if (websocket) {
        websocket.send('backward');
    }
}

function moveleft() {
    console.log("Turning left");
    if (websocket) {
        websocket.send('left');
    }
}

function moveright() {
    console.log("Turning right");
    if (websocket) {
        websocket.send('right');
    }
}

function stopmovement() {
    console.log("Stopping movement");
    if (websocket) {
        websocket.send('stop');
    }
}

function turncameraleft() {
    console.log("Turning camera left");
    if (websocket) {
        websocket.send('camera_left');
    }
}

function turncameraright() {
    console.log("Turning camera right");
    if (websocket) {
        websocket.send('camera_right');
    }
}

function turncameraup() {
    console.log("Turning camera up");
    if (websocket) {
        websocket.send('camera_up');
    }
}

function turncameradown() {
    console.log("Turning camera down");
    if (websocket) {
        websocket.send('camera_down');
    }
}

function issueAlexaCommand(command) {
    console.log("Issuing Alexa command: " + command);
    if (websocket) {
        websocket.send(command);
    }
}