const backendAccessPoint = import.meta.env.VITE_BACKEND_ACCESS_POINT;

function calculateImageSourceName(imageName){
    return `${backendAccessPoint}/images/${imageName}`;
}

export default calculateImageSourceName;