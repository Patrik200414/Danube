function appendFilesToFormData(fileName, files){
    const formData = new FormData();
    for(let i = 0; i < files.length; i++){
        formData.append(fileName, files[i]);
    }

    return formData;
}

export default appendFilesToFormData;