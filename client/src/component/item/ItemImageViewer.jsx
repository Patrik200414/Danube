import { useState } from "react";
import Proptypes from 'prop-types';
import calculateImageSourceName from "../../utility/calculateImageSourceName";

function ItemImageContainer({images}){
    const [currentImage, setCurrentImage] = useState(images.length ? calculateImageSourceName(images[0]) : '');

    function handleImageClick(index){
        setCurrentImage(calculateImageSourceName(images[index]));
    }

    return(
        <div className="item-image-container">
            <img src={currentImage} />
            <div className="uploaded-images-container">
                {images.map((image, i) => <img className="product-image" onClick={() => handleImageClick(i)} key={image + i} src={calculateImageSourceName(image)}/>)}
            </div>
        </div>
    )
}

ItemImageContainer.propTypes = {
    images: Proptypes.array
}

export default ItemImageContainer;