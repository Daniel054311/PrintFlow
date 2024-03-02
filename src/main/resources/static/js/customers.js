// Function to format date in YYYY-MM-DD HH:mm:ss format
function formatDate(dateString) {
   var date = new Date(dateString);
   var year = date.getFullYear();
   var month = String(date.getMonth() + 1).padStart(2, '0');
   var day = String(date.getDate()).padStart(2, '0');
   return year + '-' + month + '-' + day;
}


function fetchCustomers() {
   $.ajax({
      type: "GET",
      url: "/api/customers/fetch",
      success: function (customers) {

         // Clear the existing list before adding new customers
         $('#customer-list').empty();

         // Loop through each customer and append their data to the list
         customers.forEach(function (customer, index) {

            $('#customer-list').append(
               '<li>' +
               '<a href="/dashboard/preview-customer-files?customer=' + customer._id + '"  class="customer-item" data-file="' + customer.fileUrl + '">' +
               '<div class="preview-container">' +
               getFilePreviewHTML(customer.fileUrl) + // Get preview based on file type
               '</div>' +
               '<span class="product">' + customer.name + '</span>' +
               '</a>' +
               '<span class="price">Created At: ' + formatDate(customer.createdAt) + '</span>' +
               '</li>'
            );


         });

         // Function to get preview HTML based on file type
         function getFilePreviewHTML(fileUrl) {
            var extension = fileUrl.split('.').pop().toLowerCase();

            if (extension === 'pdf' || extension === 'doc' || extension === 'docx') {
               return '<img width="30px" height="30px" src="/api/files/fetch?filename=1708202239534_download.jpeg" >';
            } else {
               return '<img src="/api/files/fetch?filename=' + encodeURIComponent(fileUrl) + '" alt="">';
            }
         }

      },
      error: function (xhr, status, error) {
         console.error('Error fetching customers:', error);
      }
   });
}





// Call the fetchCustomers function to retrieve and display customer data
fetchCustomers();


