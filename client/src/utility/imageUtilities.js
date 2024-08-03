export function convertBase64ToObjectUrlForImage(base64Image){
    const binaryString = atob(base64Image);
    const bytes = new Uint8Array(binaryString.length);
    for(let i = 0; i < binaryString.length; i++){
        bytes[i] = binaryString.charCodeAt(i);
    }

    const blobedImage = new Blob([bytes]);
    return URL.createObjectURL(blobedImage);
}

export function imageUpload(uploadedFiles, images){
    const files = [];
    for(const fileIndex in uploadedFiles){
        const isImageAlreadyAdded = images.filter(image => image.imageName === uploadedFiles[fileIndex].name).length > 0;
        if(uploadedFiles[fileIndex] instanceof File && !isImageAlreadyAdded){
            files.push(uploadedFiles[fileIndex]);
        }
    }

    return files;
}