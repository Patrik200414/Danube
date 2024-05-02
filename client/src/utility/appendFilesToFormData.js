function appendFilesToFormData(formData, fileName, files){
    for(let i = 0; i < files.length; i++){
        formData.append(fileName, files[i]);
    }

    return formData;
}

export default appendFilesToFormData;