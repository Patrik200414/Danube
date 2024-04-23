import Proptypes from 'prop-types';

function ImageContainer({currImage, onImageButtonClick, isPrevButtonDisabled, isNextButtonDisabled}){
    return(
        <div className="product-image-container">
            <button className={isPrevButtonDisabled ? 'image-switch-button-disabled' : 'image-switch-button'} onClick={() => onImageButtonClick(-1)}>&#8826;</button>
            <img src={currImage}/>
            <button className={isNextButtonDisabled ? 'image-switch-button-disabled' : 'image-switch-button'} onClick={() => onImageButtonClick(1)}>&#8827;</button>
        </div>
    )
}

ImageContainer.propTypes = {
    currImage: Proptypes.string,
    onImageButtonClick: Proptypes.func,
    isPrevButtonDisabled: Proptypes.bool,
    isNextButtonDisabled: Proptypes.bool
}

export default ImageContainer;