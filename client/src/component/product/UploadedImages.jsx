import { useEffect, useState } from "react";
import Proptypes from "prop-types";
import convertBase64ToObjectUrlForImage from '../../utility/convertBase64ToObjectUrlForImage';

function UploadedImages({images, onImageDeletion}){
    const [imagesPreview, setImagesPreview] = useState([]);
    useEffect(() => {
        if(images.length){
            const blobedImages = images.map(image => {
                if(image instanceof File){
                    const blob = new Blob([image]);
                    return URL.createObjectURL(blob);
                } else{
                   return convertBase64ToObjectUrlForImage(image.imageFile); 
                }
                
            })

            setImagesPreview(blobedImages);
        } else{
            setImagesPreview(images);
        }
        
    }, [images])

    return(
        <div className="uploaded-images-container remove-preview-image-icon">
            {imagesPreview.map((imageURL, i) => (
                <div key={imageURL} className="image-preview-container">
                    <i onClick={() => onImageDeletion(i)} className="fa fa-trash-o remove-image-preview-icon"></i>
                    <img src={imageURL} className="image-preview"/>
                </div>
            ))}
        </div>
    )
}

UploadedImages.propTypes = {
    images: Proptypes.array,
    onImageDeletion: Proptypes.func
}

export default UploadedImages;