import { useState } from "react";
import Proptypes from 'prop-types';
import convertBase64ToObjectUrlForImage from "../../utility/convertBase64ToObjectUrlForImage";

function ItemImageContainer({images}){
    const [currentImage, setCurrentImage] = useState(images.length ? convertBase64ToObjectUrlForImage(images[0].imageFile) : '');

    function handleImageClick(index){
        setCurrentImage(calculateImageSourceName(images[index]));
    }

    return(
        <div className="item-image-container">
            <img src={currentImage} />
            <div className="uploaded-images-container">
                {images.map((image, i) => <img className="product-image" onClick={() => handleImageClick(i)} key={image + i} src={convertBase64ToObjectUrlForImage(image.imageFile)}/>)}
            </div>
        </div>
    )
}

ItemImageContainer.propTypes = {
    images: Proptypes.array
}

export default ItemImageContainer;