import Proptypes from 'prop-types';

function ImageContainer({currImage, onPrevImageButtonClick, onNextImageButtonClick, isPrevButtonDisabled, isNextButtonDisabled}){
    return(
        <div className="product-image-container">
            <button disabled={isPrevButtonDisabled} className="image-pev-button" onClick={onPrevImageButtonClick}>&#8826;</button>
            <img src={currImage}/>
            <button disabled={isNextButtonDisabled} className="image-next-button" onClick={onNextImageButtonClick}>&#8827;</button>
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