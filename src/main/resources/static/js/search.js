function myFunction() {
   var input, filter, table, tr, td, i, txtValue;
   input = document.getElementById("myInput");
   filter = input.value.toUpperCase();
   table = document.getElementById("myTable");
   tr = table.getElementsByTagName("tr");
   for (i = 0; i < tr.length; i++) {
      td = tr[i].getElementsByTagName("td")[0];
      if (td) {
         txtValue = td.textContent || td.innerText;
         if (txtValue.toUpperCase().indexOf(filter) > -1) {
            tr[i].style.display = "";
         } else {
            tr[i].style.display = "none";
         }
      }
   }
}

function editRow(button) {
   // Add logic for editing the row
   alert("Edit button clicked");
}

function deleteRow(button) {
   // Add logic for deleting the row
   alert("Delete button clicked");
}

function openSearch() {
   document.getElementById("myOverlay").style.display = "block";
 }
 
 function closeSearch() {
   document.getElementById("myOverlay").style.display = "none";
 }