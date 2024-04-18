import { useState } from 'react';
import defaultProductImage from '../static/defaultProduct.jpg';
import Proptypes from 'prop-types';
import ImageContainer from './ImageContainer';

function ProductCard({product}){
    const imagesLength = product.images.length;
    console.log(imagesLength );

    const [imageNumber, setImageNumber] = useState(0);
    const [isPrevButtonDisabled, setIsPrevButtonDisabled] = useState(true);
    const [isNextButtonDisabled, setIsNextButtonDisabled] = useState(!(imagesLength > 1));

    function calculateImageSourceName(imageName){
        return `http://localhost:8080/images/${imageName}`;
    }

    function controllPrevButtonState(imageNumber){
        if(imageNumber > 0){
            setIsPrevButtonDisabled(false);
            setImageNumber(imageNumber);
        } else{
            setIsPrevButtonDisabled(true);
        }
    }

    function controllNextButtoState(imageNumber){
        if(imageNumber < imagesLength - 1){
            setIsNextButtonDisabled(false);
            setImageNumber(imageNumber);
        } else{
            setIsNextButtonDisabled(true);
        }
    }

    function handleClickPrevImageButton(){
        const changedValue = imageNumber - 1;
        controllPrevButtonState(changedValue);
        controllNextButtoState(changedValue);
    }


    function handleClickNextImageButton(){
        const changedValue = imageNumber + 1;
        controllPrevButtonState(changedValue);
        controllNextButtoState(changedValue);
    }

    return(
        <div className="product-card">
            {imagesLength ? 
                <ImageContainer 
                    onPrevImageButtonClick={handleClickPrevImageButton}
                    onNextImageButtonClick={handleClickNextImageButton}
                    currImage={calculateImageSourceName(product.images[imageNumber])}
                    isPrevButtonDisabled={isPrevButtonDisabled}
                    isNextButtonDisabled={isNextButtonDisabled}
                /> : 
                <img src={defaultProductImage}/>
            }
            
            <h3>{product.productName}</h3>
            <h4>US ${product.price}</h4>
            <div className="sub-informations">
                <span>Shipping price: US ${product.shippingPrice}</span>
                <span>Sold: {product.sold}</span>
                <span>Quantity: {product.quantity}</span>
                <span>Delivery time in day: {product.deliveryTimeInDay}</span>
            </div>

        </div>
    )
}

ProductCard.propTypes = {
    product: Proptypes.object
}

export default ProductCard;