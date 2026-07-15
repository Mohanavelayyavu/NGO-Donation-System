// =========================================
// Backend URL
// =========================================

const BASE_URL = "http://localhost:8081";


// =========================================
// Register User
// =========================================

async function registerUser() {

    let name = document.getElementById("name").value.trim();

    let email = document.getElementById("email").value.trim();

    let password = document.getElementById("password").value.trim();

    let role = document.getElementById("role").value;

    if (name == "" ||
        email == "" ||
        password == "" ||
        role == "") {

        alert("Please fill all fields.");

        return;

    }

    const user = {

        name: name,

        email: email,

        password: password,

        role: role

    };

    try {

        const response = await fetch(BASE_URL + "/register", {

            method: "POST",

            headers: {

                "Content-Type": "application/json"

            },

            body: JSON.stringify(user)

        });

        const result = await response.text();

        alert(result);

        if (result.toLowerCase().includes("success")) {

            window.location.href = "login.html";

        }

    }
    catch (error) {

        console.error(error);

        alert("Server Connection Failed.");

    }

}



// =========================================
// Login User
// =========================================

async function loginUser() {

    let email = document.getElementById("email").value.trim();

    let password = document.getElementById("password").value.trim();

    let role = document.getElementById("role").value;

    if (email == "" ||
        password == "" ||
        role == "") {

        alert("Please fill all fields.");

        return;

    }

    const user = {

        email: email,

        password: password,

        role: role

    };

    try {

        const response = await fetch(BASE_URL + "/login", {

            method: "POST",

            headers: {

                "Content-Type": "application/json"

            },

            body: JSON.stringify(user)

        });

        const data = await response.json();

        if (data != null && data.id != null) {

            localStorage.setItem("user", JSON.stringify(data));

            alert("Login Successful");

            window.location.href = "dashboard.html";

        }
        else {

            alert("Invalid Email, Password or Role");

        }

    }
    catch (error) {

        console.error(error);

        alert("Unable to connect to the server.");

    }

}



// =========================================
// Logout
// =========================================

function logout() {

    localStorage.clear();

    window.location.href = "login.html";

}



// =========================================
// Show Dashboard Section
// =========================================

function showSection(id) {

    const sections = [

        "adminSection",

        "ngoSection",

        "donorSection",

        "beneficiarySection"

    ];

    sections.forEach(section => {

        const div = document.getElementById(section);

        if (div)

            div.style.display = "none";

    });

    document.getElementById(id).style.display = "block";

}



// =========================================
// Load Dashboard
// =========================================

function loadDashboard() {

    const user = JSON.parse(localStorage.getItem("user"));

    if (user == null) {

        window.location.href = "login.html";

        return;

    }

    document.getElementById("welcomeUser").innerHTML =

        "Welcome, " + user.name;

    if (user.role == "ADMIN") {

        showSection("adminSection");

        loadUsers();

        loadBeneficiaryApplications();

    }

    else if (user.role == "NGO") {

        showSection("ngoSection");

        loadRequests();

    }

    else if (user.role == "DONOR") {

        showSection("donorSection");

        loadRequests();

        loadMyDonations();

    }

    else if (user.role == "BENEFICIARY") {

        showSection("beneficiarySection");

        loadRequests();

        loadMyApplications();

    }

}
// =========================================
// Load Users
// =========================================

async function loadUsers() {

    try {

        const response = await fetch(BASE_URL + "/users");

        const users = await response.json();

        let rows = "";

        users.forEach(function (u) {

            rows += `

            <tr>

                <td>${u.id}</td>

                <td>${u.name}</td>

                <td>${u.email}</td>

                <td>${u.role}</td>

            </tr>

            `;

        });

        if (document.getElementById("userTable")) {

            document.getElementById("userTable").innerHTML = rows;

        }

        if (document.getElementById("userCount")) {

            document.getElementById("userCount").innerHTML = users.length;

        }

    }

    catch (error) {

        console.log(error);

    }

}



// =========================================
// Load Donation Requests
// =========================================

async function loadRequests() {

    try {

        const response = await fetch(BASE_URL + "/requests");

        const requests = await response.json();

        let ngoRows = "";

        let donorRows = "";

        let beneficiaryRows = "";

        requests.forEach(function (r) {

            ngoRows += `

            <tr>

                <td>${r.id}</td>

                <td>${r.title}</td>

                <td>${r.targetAmount}</td>

                <td>${r.collectedAmount}</td>

                <td>${r.status}</td>

            </tr>

            `;

            donorRows += `

            <tr>

                <td>${r.id}</td>

                <td>${r.title}</td>

                <td>${r.targetAmount}</td>

                <td>${r.collectedAmount}</td>

                <td>

                    <button class="btn btn-success btn-sm"

                        onclick="donate(${r.id})">

                        Donate

                    </button>

                </td>

            </tr>

            `;

            beneficiaryRows += `

            <tr>

                <td>${r.id}</td>

                <td>${r.title}</td>

                <td>${r.description}</td>

                <td>${r.targetAmount}</td>

                <td>${r.collectedAmount}</td>

                <td>${r.status}</td>

            </tr>

            `;

        });

        if (document.getElementById("requestTable")) {

            document.getElementById("requestTable").innerHTML = ngoRows;

        }

        if (document.getElementById("donorRequestTable")) {

            document.getElementById("donorRequestTable").innerHTML = donorRows;

        }

        if (document.getElementById("beneficiaryTable")) {

            document.getElementById("beneficiaryTable").innerHTML = beneficiaryRows;

        }

        if (document.getElementById("requestCount")) {

            document.getElementById("requestCount").innerHTML = requests.length;

        }

    }

    catch (error) {

        console.log(error);

    }

}



// =========================================
// Add Donation Request
// =========================================

async function addRequest() {

    const user = JSON.parse(localStorage.getItem("user"));

    const request = {

        ngoId: user.id,

        title: document.getElementById("title").value,

        description: document.getElementById("description").value,

        targetAmount: document.getElementById("targetAmount").value

    };

    try {

        await fetch(BASE_URL + "/requests", {

            method: "POST",

            headers: {

                "Content-Type": "application/json"

            },

            body: JSON.stringify(request)

        });

        alert("Donation Request Added Successfully");

        document.getElementById("title").value = "";

        document.getElementById("description").value = "";

        document.getElementById("targetAmount").value = "";

        loadRequests();

    }

    catch (error) {

        console.log(error);

    }

}



// =========================================
// Donate
// =========================================

async function donate(requestId) {

    const amount = prompt("Enter Donation Amount");

    if (amount == null || amount == "") {

        return;

    }

    const user = JSON.parse(localStorage.getItem("user"));

    const donation = {

        donorId: user.id,

        requestId: requestId,

        amount: amount

    };

    try {

        await fetch(BASE_URL + "/donate", {

            method: "POST",

            headers: {

                "Content-Type": "application/json"

            },

            body: JSON.stringify(donation)

        });

        alert("Donation Successful");

        loadRequests();

        loadMyDonations();

    }

    catch (error) {

        console.log(error);

    }

}
// =========================================
// Apply Beneficiary
// =========================================



async function applyBeneficiary() {

    const user = JSON.parse(localStorage.getItem("user"));

    const beneficiary = {

        userId: user.id,

        name: document.getElementById("beneficiaryName").value,

        purpose: document.getElementById("purpose").value,

        requiredAmount: document.getElementById("requiredAmount").value,

        description: document.getElementById("beneficiaryDescription").value,

        status: "PENDING"

    };

    try {

        const response = await fetch(BASE_URL + "/beneficiaries", {

            method: "POST",

            headers: {

                "Content-Type": "application/json"

            },

            body: JSON.stringify(beneficiary)

        });

        const result = await response.text();

        alert(result);

        document.getElementById("beneficiaryName").value = "";

        document.getElementById("purpose").value = "";

        document.getElementById("requiredAmount").value = "";

        document.getElementById("beneficiaryDescription").value = "";

        loadMyApplications();

    }

    catch (error) {

        console.log(error);

    }

}



// =========================================
// Load My Applications
// =========================================

async function loadMyApplications() {

    const user = JSON.parse(localStorage.getItem("user"));

    try {

        const response = await fetch(BASE_URL + "/beneficiaries");

        const applications = await response.json();

        let rows = "";

        applications.forEach(function(app){

            if(app.userId == user.id){

                rows += `

                <tr>

                    <td>${app.id}</td>

                    <td>${app.purpose}</td>

                    <td>${app.amount}</td>

                    <td>${app.description}</td>

                    <td>${app.status}</td>

                </tr>

                `;

            }

        });

        if(document.getElementById("beneficiaryApplicationTable")){

            document.getElementById("beneficiaryApplicationTable").innerHTML = rows;

        }

    }

    catch(error){

        console.log(error);

    }

}



// =========================================
// Admin - Load Applications
// =========================================

async function loadBeneficiaryApplications() {

    try {

        const response = await fetch(BASE_URL + "/beneficiaries");

        const applications = await response.json();

        let rows = "";

        applications.forEach(function(app){

            rows += `

            <tr>

                <td>${app.id}</td>

                <td>${app.userId}</td>

                <td>${app.name}</td>

                <td>${app.purpose}</td>

                <td>${app.amount}</td>

                <td>${app.status}</td>

                <td>

                    <button class="btn btn-success btn-sm"

                        onclick="updateStatus(${app.id},'APPROVED')">

                        Approve

                    </button>

                    <button class="btn btn-danger btn-sm ms-2"

                        onclick="updateStatus(${app.id},'REJECTED')">

                        Reject

                    </button>

                </td>

            </tr>

            `;

        });

        if(document.getElementById("beneficiaryAdminTable")){

            document.getElementById("beneficiaryAdminTable").innerHTML = rows;

        }

    }

    catch(error){

        console.log(error);

    }

}



// =========================================
// Update Beneficiary Status
// =========================================

async function updateStatus(id, status) {

    try {

        await fetch(BASE_URL + "/beneficiaries/" + id + "/" + status, {

            method: "PUT"

        });

        alert("Application " + status);

        loadBeneficiaryApplications();

    }

    catch(error){

        console.log(error);

    }

}

async function loadMyDonations() {

    const user = JSON.parse(localStorage.getItem("user"));

    try {

        const reqResponse = await fetch(BASE_URL + "/requests");

        const requests = await reqResponse.json();

        const requestMap = {};

        requests.forEach(r => {

            requestMap[r.id] = r.title;

        });

        const response = await fetch(BASE_URL + "/donations/donor/" + user.id);

        const donations = await response.json();

        let rows = "";

        donations.forEach(d => {

            const title = requestMap[d.requestId] || ("Request #" + d.requestId);

            rows += `

            <tr>

                <td>${title}</td>

                <td>₹ ${d.amount}</td>

                <td>${d.donationDate || 'N/A'}</td>

            </tr>

            `;

        });

        if (document.getElementById("donationHistoryTable")) {

            document.getElementById("donationHistoryTable").innerHTML = rows;

        }

    } catch (error) {

        console.error("Error loading donations:", error);

    }

}