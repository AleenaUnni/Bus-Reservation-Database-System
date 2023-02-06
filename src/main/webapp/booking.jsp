<%@ page import="Initializer.ConnectionHandler" %>
<%@ page import="java.util.Map" %><%--
  Created by IntelliJ IDEA.
  User: ebin-4354
  Date: 08/01/23
  Time: 11:30 pm
  To change this template use File | Settings | File Templates.
--%>
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
    <h1 class="display-4">Hello, user!</h1>
    <p class="lead">Welcome to Green Bus console</p>
    <hr class="my-4">
    <ul class="nav nav-pills nav-fill">
        <li class="nav-item">
            <a class=" nav-link" href="#" onclick="showMyBookings()">My Bookings</a>
        </li>
        <li class="nav-item">
            <a class=" nav-link" href="#" onclick="createJourney()">Book a bus!</a>
        </li>
    </ul>
</div>

<div id="journeyInfo" class="d-none options">
    <div class="form-group form-check">
        <label for="from_station"><b>Source</b></label>
        <select name="from_station" class="form-control" id="from_station">
        </select>

        <label for="to_station"><b>Destination</b></label>
        <select name="to_station" class="form-control" id="to_station">
        </select>

        <label for="travel_date">Date:</label>
        <input type="date" class="form-control" id="travel_date" name="travel_date">
    </div>
    <button onclick="findbuses()" class="btn btn-primary ml-4">Search for buses</button>
</div>

<div id="viewBox" class="d-none options"></div>

</body>
<script>
    var user_id=<%=request.getAttribute("user_id")%>
    var busMap = {};
    var stationMap = {};

    function showMyBookings() {
        document.querySelectorAll(".options").forEach(element => element.classList.add("d-none"));

        fetch("http://localhost:8080/reservation_war_exploded/FetchBookings?user_id="+user_id, {
            "method": "GET",
            "credentials": "include"
        }).then((response) => response.json()).then(function (buses) {
            let text = "<table class='table' border='1'>"
            for (let key in buses[0]) {
                text += "<th class='p-2'>" + key + "</th>";
            }
            for (let bus of buses) {
                busMap[bus.id] = bus;
                text += "<tr>";
                for (let key in bus) {
                    text += "<td class='p-2'>" + bus[key] + "</td>";
                }
                text += "</tr>";
            }
            text += "</table>"
            const displayElement = document.getElementById("viewBox");
            displayElement.innerHTML = text;
            displayElement.classList.remove("d-none");
        });
    }
    showMyBookings();

    function findbuses() {
        const fromStationId = document.getElementById("from_station").value;
        const toStationId = document.getElementById("to_station").value;
        let travel_date = document.getElementById("travel_date").value;

        let dateParts = travel_date.split("-");
        const travel_date_long = new Date( dateParts[2], dateParts[1] - 1, dateParts[0]).getTime();
        document.querySelectorAll(".options").forEach(element => element.classList.add("d-none"));
        let dataBind = {};
        dataBind.search = true;
        dataBind.source = fromStationId;
        dataBind.destination = toStationId;
        dataBind.date = travel_date;

        fetch("http://localhost:8080/reservation_war_exploded/fetchroutes", {
            method: 'POST', // *GET, POST, PUT, DELETE, etc.
            body: JSON.stringify(dataBind) // body data type must match "Content-Type" header
        }).then((response) => response.json()).then(function (buses) {

            let text = "<table class='table'>"

            text += "<tr>";
            for (let key in buses[0]) {
                if(key!== "id"){
                    text += "<th class='p-2'>" + key + "</th>";
                }
            }
            text += "<th class='p-2'>Click here to book</th>";
            text += "</tr>";

            for (let bus of buses) {
                text += "<tr>";
                for (let key in bus) {
                    if(key!== "id"){
                        text += "<td class='p-2'>" + bus[key] + "</td>";
                    }
                }
                text += "<td class='p-2'><button onclick='persistBooking("+bus.id+")' class='btn btn-primary ml-4'>Book</button></td>";

                text += "</tr>";
            }
            text += "</table>"
            const displayElement = document.getElementById("viewBox");
            displayElement.innerHTML = text;
            displayElement.classList.remove("d-none");
        });
    }

    function createJourney(){
        document.querySelectorAll(".options").forEach(element => element.classList.add("d-none"));
        fetch("http://localhost:8080/reservation_war_exploded/fetchstations", {
            "method": "GET",
            "credentials": "include"
        }).then((response) => response.json()).then(function (response) {
            if (response.length) {
                const fromStationElement = document.getElementById("from_station");
                const toStationElement = document.getElementById("to_station");

                response.forEach(station => {
                    stationMap[station.id] = station;
                    fromStationElement.options.add(new Option(station.name, station.id));
                    toStationElement.options.add(new Option(station.name, station.id));
                })
            }
        });
        document.getElementById("journeyInfo").classList.remove("d-none");
    }

    function persistBooking(bus_id){

        let dataBind = {};
        dataBind.bus_id = bus_id;
        dataBind.user_id = user_id;
        dataBind.date = document.getElementById("travel_date").value;

        fetch("http://localhost:8080/reservation_war_exploded/persistbooking", {
            body: JSON.stringify(dataBind),
            "method": "POST",
            "credentials": "include"
        });
    }

</script>
</html>
