document.addEventListener('DOMContentLoaded', () => {
   const fileInput = document.getElementById('fileInput');
   const fileDrop = document.getElementById('fileDrop');
   const selectedFiles = document.getElementById('selectedFiles');
   const scanButton = document.getElementById('scanButton');
   const fileCounter = document.getElementById('fileCounter');

   // Overall base drag and drop
   document.body.addEventListener('dragover', (e) => {
      e.preventDefault();
      handleFiles(e.dataTransfer.files);
   });

   document.body.addEventListener('drop', (e) => {
      e.preventDefault();
      handleFiles(e.dataTransfer.files);
   });

   fileDrop.addEventListener('dragover', (e) => {
      e.preventDefault();
      fileDrop.classList.add('drag-over');
   });

   fileDrop.addEventListener('dragleave', () => {
      fileDrop.classList.remove('drag-over');
   });

   fileDrop.addEventListener('drop', (e) => {
      e.preventDefault();
      fileDrop.classList.remove('drag-over');
      handleFiles(e.dataTransfer.files);
      e.stopPropagation(); // Stop propagation to prevent duplication
   });

   fileInput.addEventListener('change', (e) => {
      handleFiles(e.target.files);
   });

   scanButton.addEventListener('click', () => {
      openCameraAndCapture();
   });

   async function openCameraAndCapture() {
      try {
         const stream = await navigator.mediaDevices.getUserMedia({ video: true });

         const video = document.createElement('video');
         document.body.appendChild(video);
         video.srcObject = stream;

         video.onloadedmetadata = (e) => {
            video.play();
         };

         // Capture image from video when the user clicks "Scan"
         video.addEventListener('click', () => {
            captureImageFromVideo(video);
         });
      } catch (error) {
         console.error('Error accessing camera:', error);
      }
   }

   function captureImageFromVideo(video) {
      const canvas = document.createElement('canvas');
      const context = canvas.getContext('2d');

      canvas.width = video.videoWidth;
      canvas.height = video.videoHeight;

      context.drawImage(video, 0, 0, canvas.width, canvas.height);

      const imageDataURL = canvas.toDataURL('image/png');

      const fileName = `scannedImage_${new Date().getTime()}.pdf`;
      const fileInfo = { name: fileName, dataURL: imageDataURL };

      convertImageToPDF(fileInfo);
   }

   function convertImageToPDF(fileInfo) {
      const pdfOptions = { margin: 10, filename: fileInfo.name };

      html2pdf().from(fileInfo.dataURL).set(pdfOptions).outputPdf(pdf => {
         const pdfBlob = new Blob([pdf], { type: 'application/pdf' });

         appendFileRow({ name: fileInfo.name, file: pdfBlob });
      });
   }


   const existingFileNames = [];
   let totalFiles = 0;

   function updateFileCounter() {
      fileCounter.textContent = `Total Files: ${totalFiles}`;
   }

   function handleFiles(files) {
      if (files.length > 0) {
         for (const file of files) {
            if (isValidFileType(file)) {
               const fileName = file.name;

               // Check if the file with the same name already exists
               if (existingFileNames.includes(fileName)) {
                  // Handle duplicate file, you can choose to override or block
                  console.log(`File with name ${fileName} already exists!`);
                  continue; // Skip processing the duplicate file
               }

               existingFileNames.push(fileName); // Add the filename to the list
               totalFiles += 1; // Increment the total files counter

               if (file.type === 'application/zip') {
                  unzipFile(file);
               } else {
                  const fileInfo = {
                     name: fileName,
                     type: file.type,
                     file: file
                  };
                  appendFileRow(fileInfo);
               }
            }
         }

         updateFileCounter(); // Update the file counter after handling files
      }
   }

   function isValidFileType(file) {
      const acceptedTypes = ['image', 'application/pdf', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document', 'application/ppt', 'application/msword', 'application/zip', 'text/plain'];
      return acceptedTypes.some(type => file.type.startsWith(type)) && file.type !== 'image/svg+xml';
   }

   function appendFileRow(fileInfo) {
      const fileRow = document.createElement('div');
      fileRow.classList.add('file-row');

      const fileInfoDiv = document.createElement('div');
      fileInfoDiv.textContent = `Selected File: ${fileInfo.name}`;
      fileRow.appendChild(fileInfoDiv);

      const removeButton = document.createElement('div');
      removeButton.classList.add('remove-file');
      removeButton.textContent = 'X';
      removeButton.addEventListener('click', () => {
         fileRow.remove();
         removeFileFromStorage(fileInfo);
      });
      fileRow.appendChild(removeButton);

      selectedFiles.appendChild(fileRow);

      // Store the file info in local storage
      const storedFiles = JSON.parse(localStorage.getItem('selectedFiles')) || [];
      storedFiles.push(fileInfo);
      localStorage.setItem('selectedFiles', JSON.stringify(storedFiles));
   }

   async function uploadFile(fileBlob, fileInfo) {
      // Your upload logic
   }

   function removeFileFromStorage(fileInfo) {
      const storedFiles = JSON.parse(localStorage.getItem('selectedFiles')) || [];
      const updatedFiles = storedFiles.filter(file => file.name !== fileInfo.name);
      localStorage.setItem('selectedFiles', JSON.stringify(updatedFiles));
   }

   function unzipFile(zipFile) {
      // Add your unzip logic here
      // For simplicity, let's assume it returns an array of file blobs
      const unzippedFiles = [];

      for (const file of unzippedFiles) {
         const fileName = file.name;
         const fileInfo = { name: fileName };
         appendFileRow(fileInfo);
      }
   }
});
const submitButton = document.getElementById('submitButton');
submitButton.addEventListener('click', () => {
   const storedFiles = JSON.parse(localStorage.getItem('selectedFiles')) || [];

   // Prepare file data for submission (you may need to adjust this based on your server requirements)
   const formData = new FormData();
   storedFiles.forEach(fileInfo => {
      formData.append('file', fileInfo.file);

      console.log(fileInfo.file)
   });

   console.log(formData)

   // Example: Send the files using jQuery AJAX
   $.ajax({
      url: '/api/files/upload',
      type: 'POST',
      data: formData,
      processData: false,
      contentType: false,
      success: function (response) {
         console.log('Files submitted successfully:', response);
         // Optionally, you can clear the selected files after submission
         localStorage.removeItem('selectedFiles');
         updateFileCounter(); // Update the file counter after handling files
         selectedFiles.innerHTML = ''; // Clear the displayed files
         // Handle success scenario
         swal("Good job!", response, "success");
      },
      error: function (xhr, textStatus, errorThrown) {
         //console.error("Error loggin user:", errorThrown);
         if (xhr.status >= 400 && xhr.status <= 499) {

            swal("Oops!", "Files not sent succesfully , Please try again", "error");

         } else if (xhr.status >= 500) {
            swal("Oops!", "Somthing happened , please try again.", "error");

         }
      }
   });
});