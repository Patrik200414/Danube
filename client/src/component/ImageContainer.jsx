import Proptypes from 'prop-types';

function ImageContainer({currImage, onPrevImageButtonClick, onNextImageButtonClick, isPrevButtonDisabled, isNextButtonDisabled}){
    return(
        <div className="product-image-container">
            <button className={isPrevButtonDisabled ? 'image-switch-button-disabled' : 'image-switch-button'} onClick={onPrevImageButtonClick}>&#8826;</button>
            <img src={currImage}/>
            <button className={isNextButtonDisabled ? 'image-switch-button-disabled' : 'image-switch-button'} onClick={onNextImageButtonClick}>&#8827;</button>
        </div>
    )
}

ImageContainer.propTypes = {
    currImage: Proptypes.string,
    onPrevImageButtonClick: Proptypes.func,
    onNextImageButtonClick: Proptypes.func,
    isPrevButtonDisabled: Proptypes.bool,
    isNextButtonDisabled: Proptypes.bool
}

export default ImageContainer;