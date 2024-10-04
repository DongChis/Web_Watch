/**
 * 
 */
// JavaScript to add hover interaction
document.querySelectorAll('.box-data').forEach(function(box) {
    box.addEventListener('mouseover', function() {
        box.style.backgroundColor = '#f9f9f9'; // Change background on hover
    });
    box.addEventListener('mouseout', function() {
        box.style.backgroundColor = '#fff'; // Reset background after hover
    });
});
