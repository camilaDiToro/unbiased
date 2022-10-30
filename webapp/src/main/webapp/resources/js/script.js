$(document).on('click', '#categories-dropdown', function (e) {
    e.stopPropagation();
});

const fun = (event) => {
    if(event.keyCode === 13){
        $("#search-form").submit();
    }
}

const query= document.getElementById('query');
console.log(query)
query.addEventListener('keyup', fun);



$(function () {
    $('[data-toggle="tooltip"]').tooltip()
});
