// // $(document).on('click', '#categories-dropdown', function (e) {
// //     e.stopPropagation();
// // });
//
// const fun = (event) => {
//     if(event.keyCode === 13){
//         $("#search-form").submit();
//     }
// }
//
// const query= document.getElementById('query');
// console.log(query)
// query.addEventListener('keyup', fun);
//
//
//
// $(function () {
//     $('[data-toggle="tooltip"]').tooltip()
// });
//
// $('#fileInput').on('change',function(){
//     //get the file name
//     var fileName = $(this).val();
//     //replace the "Choose a file" label
//     $(this).next('.custom-file-label').html(fileName);
// })
//
// $('#file-input').on('change',function(){
//     //get the file name
//     var fileName = $(this).val();
//     //replace the "Choose a file" label
//     $(this).next('.custom-file-label').html(fileName);
// })

$(document).ready(() => $('[data-toggle=tooltip]').tooltip());
