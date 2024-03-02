const { value: text } = await Swal.fire({
   input: "textarea",
   inputLabel: "Message",
   inputPlaceholder: "Type your message here...",
   inputAttributes: {
     "aria-label": "Type your message here"
   },
   showCancelButton: true
 });
 if (text) {
   Swal.fire(text);
 }


 /* inputOptions can be an object or Promise */
const inputOptions = new Promise((resolve) => {
  setTimeout(() => {
    resolve({
      "#ff0000": "Red",
      "#00ff00": "Green",
      "#0000ff": "Blue"
    });
  }, 1000);
});
const { value: color } = await Swal.fire({
  title: "Select color",
  input: "radio",
  inputOptions,
  inputValidator: (value) => {
    if (!value) {
      return "You need to choose something!";
    }
  }
});
if (color) {
  Swal.fire({ html: `You selected: ${color}` });
}

const { value: formValues } = await Swal.fire({
  title: "Multiple inputs",
  html: `
    <input id="swal-input1" class="swal2-input">
    <input id="swal-input2" class="swal2-input">
  `,
  focusConfirm: false,
  preConfirm: () => {
    return [
      document.getElementById("swal-input1").value,
      document.getElementById("swal-input2").value
    ];
  }
});
if (formValues) {
  Swal.fire(JSON.stringify(formValues));
}