

const blobUrl = '';
function getCustomerIdFromUrl() {
   // Get the current URL
   var url = window.location.href;

   // Parse the URL to get the query parameters
   var queryParams = new URLSearchParams(url.split('?')[1]);

   // Get the value of the 'customer' parameter
   var customerId = queryParams.get('customer');

   return customerId;
}
// Function to format date in YYYY-MM-DD HH:mm:ss format
function formatDate(dateString) {
   var date = new Date(dateString);
   var year = date.getFullYear();
   var month = String(date.getMonth() + 1).padStart(2, '0');
   var day = String(date.getDate()).padStart(2, '0');
   return year + '-' + month + '-' + day;
}
// Usage example
var _id = getCustomerIdFromUrl();
var fileInfo = '';
// Function to handle file preview
function previewFile(fileUrl) {
   var extension = fileUrl.split('.').pop().toLowerCase();
   var filePreviewContent = $('#filePreviewContent');
   fileInfo = fileUrl;

   // Clear any existing content
   filePreviewContent.empty();
   // Set the src attribute of your iframe to the blob URL
   // $('#Frame').attr('src', '/api/files/fetch?filename=' + fileUrl);

}



function fetchCustomers() {
   $.ajax({
      type: "GET",
      url: "/api/customers/findById?_id=" + _id,
      success: function (customer) {

         $("#customerName").text(customer.name);
         $("#customertel").text(customer.tel);

         $.ajax({
            type: "GET",
            url: "/api/customers/find/Files/ByRefId?refId=" + customer.refId,
            success: function (files) {
               $('#customer-list').empty();
               files.forEach(function (file, index) {
                  $('#customer-list').append(
                     '<li>' +
                     '<a href="#" class="customer-item" onclick="previewFile(\'' + file.name + '\')" data-file="' + file.name + '">' +
                     '<div class="preview-container">' +
                     getFilePreviewHTML(file.name) +
                     '</div>' +
                     '<span class="product">' + file.name + '</span>' +
                     '</a>' +
                     '<span class="price">Created At: ' + formatDate(file.createdAt) + '</span>' +
                     '</li>'
                  );


               });

            }
            ,
            error: function (xhr, status, error) {
               console.error('Error fetching customer files :', error);
            }
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

function fetchCustomersFiles() {
   $.ajax({
      type: "GET",
      url: "/api/customers/findById?_id=" + _id,
      success: function (customer) {
         $.ajax({
            type: "GET",
            url: "/api/customers/find/Files/ByRefId?refId=" + customer.refId,
            success: function (files) {
               $('#myTable tbody').empty(); // Clear existing table rows
               files.forEach(function (file, index) {
                  // Create a new row for each file
                  var row = $("<tr>");
                  // Add index number
                  row.append("<td>" + (index + 1) + "</td>");
                  // Add file name
                  row.append("<td>" + file.name + "</td>");
                  // Add action buttons for download and print
                  var actions = $("<td>");
                  var downloadBtn = $("<button>").text("Download").click(function () {
                     downloadFile(file.name); // Call function to download the file
                  });
                  var printBtn = $("<button>").text("Print").click(function () {
                     openPrintModal(file.name); // Call function to open print modal
                  });
                  var previewBtn = $("<button>").text("Preview").click(function () {
                     openPreview(file.name); // Call function to open print modal
                  });
                  actions.append(downloadBtn).append(printBtn).append(previewBtn);
                  row.append(actions);
                  // Append the row to the table
                  $('#myTable tbody').append(row);
               });
            },
            error: function (xhr, status, error) {
               console.error('Error fetching customer files:', error);
            }
         });
      },
      error: function (xhr, status, error) {
         console.error('Error fetching customers:', error);
      }
   });
}

// Function to download the file
function downloadFile(fileName) {
   $.ajax({
      type: "GET",
      url: '/api/files/fetch?filename=' + fileName,
      xhrFields: {
         responseType: 'blob'
      },
      success: function (blob) {
         var url = URL.createObjectURL(blob);
         var link = document.createElement('a');
         link.href = url;
         link.download = fileName;
         document.body.appendChild(link);
         link.click();
         document.body.removeChild(link);
      },
      error: function (xhr, status, error) {
         console.error('Error downloading file:', error);
      }
   });
}



function openPreview(fileName) {
   $.ajax({
      type: "GET",
      url: '/api/files/raw?filename=' + fileName,
      xhrFields: {
         responseType: 'blob'
      },
      timeout: 60000, 
      success: function (blob) {
         var fileUrl = URL.createObjectURL(blob);
         // window.open(fileUrl);
         console.log(fileUrl)
      },
      error: function (xhr, status, error) {
         console.error('Error previewing file:', error);
      }
   });

}
// Function to open print modal
function openPrintModal(fileUrl) {
   // Create a Bootstrap modal
   var modal = $('<div class="modal fade" tabindex="-1" role="dialog">' +
      '<div class="modal-dialog" role="document">' +
      '<div class="modal-content">' +
      '<div class="modal-header">' +
      '<h5 class="modal-title">Print File</h5>' +
      '<button type="button" class="close" data-dismiss="modal" aria-label="Close">' +
      '<span aria-hidden="true">&times;</span>' +
      '</button>' +
      '</div>' +
      '<div class="modal-body">' +
      // Embed the file content in an iframe
      '<iframe id="printFrame" src="' + fileUrl + '" style="width:100%;height:500px;"></iframe>' +
      '</div>' +
      '<div class="modal-footer">' +
      '<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>' +
      '<button type="button" class="btn btn-primary" onclick="printFile()">Print</button>' +
      '</div>' +
      '</div>' +
      '</div>' +
      '</div>');

   // Append the modal to the body and show it
   $('body').append(modal);
   modal.modal('show');
}

// Function to print the file content
function printFile() {
   // Print the content of the iframe
   window.frames["printFrame"].focus();
   window.frames["printFrame"].print();
}

fetchCustomersFiles();
