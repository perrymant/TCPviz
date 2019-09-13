$('.absolute').hide();
$('#numberSystemA').click(function(){
sessionStorage.setItem('numberSystem', 'true');
$('.relative, .absolute').toggle();
});

$('.absolute').hide();
$('#numberSystemR').click(function(){
sessionStorage.setItem('numberSystem', 'false');
$('.relative, .absolute').toggle();
});

window.onload = function(){
    var numberSystemClicked = sessionStorage.getItem('numberSystem');
    if (numberSystemClicked == 'true'){
        $('.relative, .absolute').toggle();
    }
}