<%-- Created by IntelliJ IDEA. User: ebin-4354 Date: 09/01/23 Time: 12:54 am To change this template use File | Settings | File Templates. --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>
    <title>Title</title>
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
            integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n"
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
            integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
            crossorigin="anonymous"></script>

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.4.1/dist/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.4.1/dist/js/bootstrap.min.js"
            integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
            crossorigin="anonymous"></script>
</head>

<body>
<div class="jumbotron">
    <h1 class="display-4">Hello, admin!</h1>
    <p class="lead">Welcome to Green Bus console</p>
    <hr class="my-4">
    <ul class="nav nav-pills nav-fill">
        <li class="nav-item">
            <a class="nav-link" href="#" onclick="showBusCreation()">Add Bus</a>
        </li>
        <li class="nav-item">
            <a class=" nav-link" href="#" onclick="showStationCreation()">Add Station</a>
        </li>
        <li class="nav-item">
            <a class=" nav-link" href="#" onclick="showRouteCreation()">Add Route</a>
        </li>
        <li class="nav-item">
            <a class=" nav-link" href="#" onclick="showBuses()">List Buses</a>
        </li>
        <li class="nav-item">
            <a class=" nav-link" href="#" onclick="showStations()">List Stations</a>
        </li>
        <li class="nav-item">
            <a class=" nav-link" href="#" onclick="showRoutes()">List Routes</a>
        </li>
    </ul>
</div>

<div id="createbusform" class="options">
    <div class="form-group form-check">
        <label for="bus_name"><b>Number of the bus</b></label>
        <input type="text" class="form-control" placeholder="Enter the name of the bus" name="bus_name" id="bus_name"
               required>
    </div>
    <div class="form-group form-check">
        <label for="bus_capacity"><b>Number of seats</b></label>
        <input type="text" class="form-control" placeholder="Enter the number of seats" name="bus_cost"
               id="bus_capacity" required>
    </div>
    <div class="form-group form-check">
        <label for="bus_cost"><b>Cost per seat</b></label>
        <input type="text" class="form-control" placeholder="Enter the cost per seat" name="bus_cost" id="bus_cost"
               required>
    </div>
    <button onclick="persistBus()" class="btn btn-primary ml-4">Create Bus</button>
</div>

<div id="createstationform" class="d-none options">
    <div class="form-group form-check">
        <label for="station_name"><b>Name of the station</b></label>
        <input type="text" class="form-control" placeholder="Enter the name of the station" name="station_name"
               id="station_name" required>
    </div>
    <button onclick="persistStation()" class="btn btn-primary ml-4">Create Station</button>
</div>

<div id="createrouteform" class="d-none options">
    <div class="form-group form-check">
        <label for="bus_select"><b>Select the bus</b></label>
        <select name="bus_select" class="form-control" id="bus_select"></select>

        <label for="from_station"><b>Source</b></label>
        <select name="from_station" class="form-control" id="from_station">
        </select>

        <label for="to_station"><b>Destination</b></label>
        <select name="to_station" class="form-control" id="to_station">
        </select>
    </div>
    <button onclick="persistRoute()" class="btn btn-primary ml-4">Create Route</button>
</div>

<div id="viewBox" class="d-none options"></div>
</body>
<script>
    function showBusCreation() {
        document.querySelectorAll(".options").forEach(element => element.classList.add("d-none"));
        document.getElementById("createbusform").classList.remove("d-none")
    }

    function persistBus() {
        const bus_name = document.getElementById("bus_name").value;
        const bus_capacity = document.getElementById("bus_capacity").value;
        const bus_cost = document.getElementById("bus_cost").value;
        fetch("http://localhost:8080/reservation_war_exploded/persistbus", {
            "body": "bus_name=" + bus_name + "&bus_capacity=" + bus_capacity + "&bus_cost=" + bus_cost + "",
            "method": "POST",
            "credentials": "include"
        });
    }

    function showStationCreation() {
        document.querySelectorAll(".options").forEach(element => element.classList.add("d-none"));
        document.getElementById("createstationform").classList.remove("d-none")
    }

    function persistStation() {
        const station_name = document.getElementById("station_name").value;
        fetch("http://localhost:8080/reservation_war_exploded/persiststation", {
            "body": "station_name=" + station_name,
            "method": "POST",
            "credentials": "include"
        });
    }

    function showRouteCreation() {
        document.querySelectorAll(".options").forEach(element => element.classList.add("d-none"));
        fetch("http://localhost:8080/reservation_war_exploded/fetchbuses", {
            "method": "GET",
            "credentials": "include"
        }).then((response) => response.json()).then(function (response) {
            const optionsElement = document.getElementById("bus_select");
            response.forEach(bus => {
                optionsElement.options.add(new Option(bus.name, bus.id));
            })

            fetch("http://localhost:8080/reservation_war_exploded/fetchstations", {
                "method": "GET",
                "credentials": "include"
            }).then((response) => response.json()).then(function (response) {
                $("#from_station").empty();
                $("#to_station").empty();
                const fromStationElement = document.getElementById("from_station");
                const toStationElement = document.getElementById("to_station");
                response.forEach(station => {
                    fromStationElement.options.add(new Option(station.name, station.id));
                    toStationElement.options.add(new Option(station.name, station.id));
                })
                document.getElementById("createrouteform").classList.remove("d-none")
            });
        });
    }

    function persistRoute() {
        const bus_id = document.getElementById("bus_select").value;
        const from_station = document.getElementById("from_station").value;
        const to_station = document.getElementById("to_station").value;
        fetch("http://localhost:8080/reservation_war_exploded/persistroute", {
            "body": "bus_id=" + bus_id + "&from_station=" + from_station + "&to_station=" + to_station + "",
            "method": "POST",
            "credentials": "include"
        });
    }

    function showBuses() {
        document.querySelectorAll(".options").forEach(element => element.classList.add("d-none"));

        fetch("http://localhost:8080/reservation_war_exploded/fetchbuses", {
            "method": "GET",
            "credentials": "include"
        }).then((response) => response.json()).then(function (buses) {
            let text = "<table class='table' border='1'>"
            for (let key in buses[0]) {
                text += "<th class='p-2'>" + key + "</th>";
            }
            for (let route of buses) {
                text += "<tr>";
                for (let key in route) {
                    text += "<td class='p-2'>" + route[key] + "</td>";
                }
                text += "</tr>";
            }
            text += "</table>"
            const displayElement = document.getElementById("viewBox");
            displayElement.innerHTML = text;
            displayElement.classList.remove("d-none");
        });
    }

    function showStations() {
        document.querySelectorAll(".options").forEach(element => element.classList.add("d-none"));

        fetch("http://localhost:8080/reservation_war_exploded/fetchstations", {
            "method": "GET",
            "credentials": "include"
        }).then((response) => response.json()).then(function (stations) {
            let text = "<table class='table' border='1'>"
            for (let key in stations[0]) {
                text += "<th class='p-2'>" + key + "</th>";
            }
            text += "</tr>";
            for (let route of stations) {
                text += "<tr>";
                for (let key in route) {
                    text += "<td class='p-2'>" + route[key] + "</td>";
                }
                text += "</tr>";
            }
            text += "</table>"
            const displayElement = document.getElementById("viewBox");
            displayElement.innerHTML = text;
            displayElement.classList.remove("d-none");
        });
    }

    function showRoutes() {
        document.querySelectorAll(".options").forEach(element => element.classList.add("d-none"));

        fetch("http://localhost:8080/reservation_war_exploded/fetchroutes", {
            "method": "GET",
            "credentials": "include"
        }).then((response) => response.json()).then(function (routes) {

            let text = "<table class='table'>"

            text += "<tr>";
            for (let key in routes[0]) {
                text += "<th class='p-2'>" + key + "</th>";
            }
            text += "</tr>";

            for (let route of routes) {
                text += "<tr>";
                for (let key in route) {
                    text += "<td class='p-2'>" + route[key] + "</td>";
                }
                text += "</tr>";
            }
            text += "</table>"
            const displayElement = document.getElementById("viewBox");
            displayElement.innerHTML = text;
            displayElement.classList.remove("d-none");
        });
    }
</script>
</html>