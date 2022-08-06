const form = document.querySelector('form');
const inputFile = form.querySelector('.file-input');
let file = null

form.addEventListener('click', () => {
    inputFile.click();
})

inputFile.onchange = (e) => {
    file = e.target.files[0];
    if (file) {
        const fileDetails = document.querySelector('.row')
        fileDetails.innerHTML = `<div class="content">
        <i class="fa-solid fa-file"></i>
        <div class="details">
          <span class="name">${file.name}</span>
        </div>
      </div>
      <span class="size">${getDataSizeWithUnit(file.size)}</span>`
        document.getElementById('fileDisplayArea').style.display = 'block';
    }
}

function getDataSizeWithUnit(size) {
    const units = ['KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
    let sizeWithUnit = "";
    let number = size;
    let count = 0;
    if (number < 1024) {
        return number + "B";
    }
    while (true) {
        if (number >= 1024) {
            number = number / 1024;
            sizeWithUnit = (Math.round(number * 100) / 100).toFixed(2) + " " + units[count];
            count++;
        } else {
            break;
        }
    }
    return sizeWithUnit;
}


async function upload() {
    const formData = new FormData();
    formData.append("file", file);

    const requestOptions = {
        method: 'POST',
        body: formData,
        redirect: 'follow'
    };

    fetch("/api/file/upload", requestOptions)
        .then(response => response.text())
        .then(result => console.log(result))
        .catch(error => console.log('error', error));
}

function addValuesToTable(data) {
    let hmtl = "";
    const table = document.getElementById('table')
    for (let index = 0; index < data.length; index++) {
        const element = data[index];
        hmtl = hmtl + `<tr>
    <td>${index + 1}</td>
    <td>${element.fileName}</td>
    <td>${element.fileSizeWithUnit}</td>
    <td><button onclick="download('${element.filePath}')"><i class="fa-solid fa-download"></i> Download</button></td>
    </tr>
   `
    }
    table.innerHTML = hmtl;
}

window.onload = onPageLoad;

async function onPageLoad() {
    fetch('/api/file/list')
        .then(response => response.json())
        .then(data => {
                addValuesToTable(data)
            }
        )
}

async function reload() {
    await onPageLoad();
}

async function download(filePath) {
    // get call api to download the file
    const url = `/api/file/download?filePath=${filePath}`
    const a = document.createElement('a');
    a.href = url;
    a.setAttribute('target', '_blank');
    a.click();
}