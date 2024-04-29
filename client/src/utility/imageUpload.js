function imageUpload(uploadedFiles, images){
    const files = [];
    for(const fileIndex in uploadedFiles){
        const isImageAlreadyAdded = images.filter(image => image.name === uploadedFiles[fileIndex].name).length > 0;
        if(typeof uploadedFiles[fileIndex] === 'object' && !isImageAlreadyAdded){
            files.push(uploadedFiles[fileIndex]);
        }
    }

    return files;
}

export default imageUpload;