import { useEffect, useState } from "react";
import Proptypes from "prop-types";

function UploadedImages({images}){
    const [imagesPreview, setImagesPreview] = useState([]);
    
    useEffect(() => {
        if(images.length){
            const blobedImages = images.map(image => {
                const blob = new Blob([image]);
                return URL.createObjectURL(blob)
            })

            setImagesPreview(blobedImages);
        }
        
    }, [images])

    return(
        <div className="uploaded-images-container">
            {imagesPreview.map(imageURL => <img key={imageURL} src={imageURL} className="image-preview"/>)}
        </div>
    )
}

UploadedImages.propTypes = {
    images: Proptypes.array
}

export default UploadedImages;