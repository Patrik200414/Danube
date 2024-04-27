const backendAccessPoint = import.meta.env.VITE_BACKEND_ACCESS_POINT;
console.log(backendAccessPoint);

function calculateImageSourceName(imageName){
    return `${backendAccessPoint}/images/${imageName}`;
}

export default calculateImageSourceName;