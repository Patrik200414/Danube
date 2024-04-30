import { useEffect, useState } from "react";
import Proptypes from "prop-types";
import calculateImageSourceName from '../../utility/calculateImageSourceName';

function UploadedImages({images, onImageDeletion}){
    const [imagesPreview, setImagesPreview] = useState([]);
    useEffect(() => {
        if(images.length){
            const blobedImages = images.map(image => {
                if(typeof image === 'object'){
                    const blob = new Blob([image]);
                    return URL.createObjectURL(blob);
                } else{
                    return calculateImageSourceName(image);
                }
                
            })

            setImagesPreview(blobedImages);
        } else{
            setImagesPreview(images);
        }
        
    }, [images])

    console.log(imagesPreview);

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