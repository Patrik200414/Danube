import Proptypes from "prop-types";

function ProductDetailsForm({details, onDetailsChange}){
    return(
        <div className="product-details-form-container">
            <h2>Product details:</h2>
                <form className="product-detail-form">
                    {Object.keys(details).map(detail => <div key={`${detail}-container`}>
                        <label htmlFor={detail}>{detail}: </label>
                        <input onChange={e => onDetailsChange(detail, e.target.value)} type="text" id={detail} name={detail} value={details[detail]} placeholder={detail}/>
                    </div>)}
                </form>

        </div>
    )
}

ProductDetailsForm.propTypes = {
    details: Proptypes.object,
    onDetailsChange: Proptypes.func
}

export default ProductDetailsForm;