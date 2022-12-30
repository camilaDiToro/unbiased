function myFunction(e) {
    var x = document.getElementById("password");
    if (x.type === "password") {
        x.type = "text";
        e.classList.add('fa-eye-slash')
    } else {
        x.type = "password";
        e.classList.remove('fa-eye-slash')
    }
}