const backendAccessPoint = import.meta.env.VITE_BACKEND_ACCESS_POINT;

function convertBase64ToObjectUrlForImage(base64Image){
    const binaryString = atob(base64Image);
    const bytes = new Uint8Array(binaryString.length);
    for(let i = 0; i < binaryString.length; i++){
        bytes[i] = binaryString.charCodeAt(i);
    }

    const blobedImage = new Blob([bytes]);
    return URL.createObjectURL(blobedImage);
}

export default convertBase64ToObjectUrlForImage;