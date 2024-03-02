

// Function to format date in YYYY-MM-DD HH:mm:ss format
function formatDate(dateString) {
   var date = new Date(dateString);
   var year = date.getFullYear();
   var month = String(date.getMonth() + 1).padStart(2, '0');
   var day = String(date.getDate()).padStart(2, '0');
   return year + '-' + month + '-' + day;
}


function fetchUsers() {
   $.ajax({
      type: "GET",
      url: "/api/users/fetch",
      success: function (users) {

         $("#myTable tbody").empty();

         users.forEach(function (user) {
            var row = $("<tr>");
            row.append($("<td>").text(user.username));
            row.append($("<td>").text(user.location));
            row.append($("<td>").text(user.tel));
            // Display the user profile image 
            var profileImage =
               $("<img>")
                  .attr("src", `/api/files/fetch?filename=${user.profile}`)
                  .attr("alt", "Profile Image")
                  .css("max-width", "50px");

            row.append($("<td>").append(profileImage));
            //row.append($("<td>").text(user.password));
            row.append($("<td>").text(user.usermail));

            // Create a button with a data attribute to store the _id
            var buttondel = $("<button>").text("Del").addClass("del-btn").attr("data-id", user._id);
            var buttonview = $("<button>").text("View").addClass("view-btn").attr("data-id", user._id);
            var buttonedit = $("<button>").text("Edit").addClass("edit-btn").attr("data-id", user._id);
            // Attach a click event handler to the button
            buttondel.click(function () {
               // Retrieve the _id from the data attribute
               var _id = $(this).data("id");

               swal({
                  title: "Are you sure?",
                  text: "Once deleted, you will not be able to recover this user!",
                  icon: "warning",
                  buttons: true,
                  dangerMode: true,
               })
                  .then((willDelete) => {
                     if (willDelete) {
                        // Attach a click event handler to the button
                        // Send API request to delete the user with the given _id
                        fetch(`/api/users/delete?_id=${_id}`, {
                           method: 'DELETE',
                        })
                           .then(response => {
                              // Check if the request was successful (status code 200-299)
                              if (response.ok) {
                                 Swal.fire("Deleted!", "", "success");
                                 fetchUsers();
                              } else {
                                 // Handle error scenarios
                                 if (response.status === 404) {
                                    swal("Hmmm!", "User not found", "warning");
                                 } else {
                                    swal("Oops!", "Error deleting user. Please try again.", "error");
                                 }
                              }
                           })
                           .catch(error => {
                              // Handle network errors or other exceptions

                              swal("Oops!", "Something went wrong. Please try again.", "error");
                           });
                     } else {
                        // User clicked "Cancel" or closed the confirmation dialog
                     }
                  });



            });



            buttonview.click(function () {
               var _id = $(this).data("id");

               // Make GET request to fetch user information
               $.get(`/api/users/search?_id=${_id}`, function (response) {
                  // Extract user information from the response
                  var htmlContent = `
                   <div>
                       <p><strong>Username:</strong> ${response.username}</p>
                       <p><strong>Location:</strong> ${response.location}</p>
                       <p><strong>Profile:</strong><img width="30px" height="30px" src="/api/files/fetch?filename=${response.profile}"/></p>
                       <p><strong>Telephone:</strong> ${response.tel}</p>
                       <p><strong>Email:</strong> ${response.usermail}</p>
                   </div>
               `;


                  // Display Swal modal with user information
                  Swal.fire({
                     title: "<strong>User Information</strong>",
                     icon: "info",
                     html: htmlContent,
                     showCloseButton: true,
                     showCancelButton: true,
                     focusConfirm: false,
                     confirmButtonText: `<i class="fa fa-thumbs-up"></i> Great!`,
                     confirmButtonAriaLabel: "Thumbs up, Okay",
                     cancelButtonText: `<i class="fa fa-thumbs-down"></i>`,
                  });
               });
            });


            buttonedit.click(function () {

               var _id = $(this).data("id");

               window.location.href = `/api/users/edit/${_id}`;


            });

            // Append the button to the row
            row.append($("<td>").append(buttondel, buttonview, buttonedit));

            // Append the row to the table
            $("#myTable tbody").append(row);
         });
      },
      error: function (xhr, textStatus, errorThrown) {
         // Handle error responses
         if (xhr.status === 400 || xhr.status === 500) {
            swal("Oops!", "Somthing happened , try again", "error");
         } else {
            swal("Oops!", "An unexpected error occurred.", "error");
         }
      }
   });
}

$(document).ready(function () {
   fetchUsers();
});

// Retrieve previous count from localStorage or initialize to 0
let previousCount = parseInt(localStorage.getItem('previousCount')) || 0;

function fetchCount() {
   $.ajax({
      type: "GET",
      url: "/api/users/fetch",
      success: function (data) {
         // Get the current count from the received data
         const currentCount = data.length;
         if (currentCount !== previousCount) {
            fetchUsers();
            // Alert with new count if there's a change
            console.log('Change detected! New count: ' + currentCount);
            // Update previous count in localStorage only when there's a change
            localStorage.setItem('previousCount', currentCount);
            //showToast("New user added ", "success", 10000);
         }
      },
      error: function (xhr, status, error) {
         console.error('Error fetching count:', error);
      }
   });
}


// Call fetchCount initially
//fetchCount();

// Periodically fetch count every second
//setInterval(fetchCount, 10000);
