function imageUpload(uploadedFiles, images){
    const files = [];
    for(const fileIndex in uploadedFiles){
        const isImageAlreadyAdded = images.filter(image => image.imageName === uploadedFiles[fileIndex].name).length > 0;
        if(uploadedFiles[fileIndex] instanceof File && !isImageAlreadyAdded){
            files.push(uploadedFiles[fileIndex]);
        }
    }

    return files;
}

export default imageUpload;