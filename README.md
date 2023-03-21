# HumVerizonUi

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 14.2.1.

## Development server

Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The application will automatically reload if you change any of the source files.

## Code scaffolding

Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive|pipe|service|class|guard|interface|enum|module`.

## Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory.

## Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

## Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via a platform of your choice. To use this command, you need to first add a package that implements end-to-end testing capabilities.

## Further help

To get more help on the Angular CLI use `ng help` or go check out the [Angular CLI Overview and Command Reference](https://angular.io/cli) page.


# Automotive Data Orchestration Platform -Dashboard User Interface

- It is a middleware service which is connect mqtt server with frontend though the api hit which connect the websocket and passes the data to frontend with filtration on the behalf of the Id's. 

- This project was generated with [Springboot] 
versions:
1) Java: 11
2) SpringBoot 2.7.4
3) Maven 4.0.0

## Installation

Use the below command to clone.

```bash
git clone https://github.com/5GRealityLab/do_dashboard_ui.git
git checkout main
git pull
cd vrsu-subscriber-manager
```

## Build the service

```bash
mvn clean 
mvn install
```

## Test the service
Hit below url :
http://localhost:8082/vrsu/api/subscriber/welcome 
You will get the welcome message **Welcome to VRSU**



