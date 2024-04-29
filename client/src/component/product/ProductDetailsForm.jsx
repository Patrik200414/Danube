import Proptypes from "prop-types";
import { useEffect } from "react";
import fetchGetAuthorization from "../../utility/fetchGetAuthorization";

function ProductDetailsForm({details, onDetailsChange, subCategoryId, user, onDetailsSet, onImageUpload}){

    useEffect(() => {
        const getDetails = async () => {
            const detailsData = await fetchGetAuthorization(`/api/product/detail/${subCategoryId}`, user.jwt);
            const detailResponse = await detailsData.json();
            onDetailsSet(detailResponse);
        }
        if(onDetailsSet){
            getDetails();
        }
    }, [subCategoryId, onDetailsSet])


    return(
        <>
        {details && 
            <div className="product-details-form-container">
                <h2>Product details:</h2>
                <form className="product-detail-form">
                    {details.map(detail => <div key={`${detail.detailName}-container`}>
                        <label htmlFor={detail.detailName}>{detail.detailName}: </label>
                        <input onChange={e => onDetailsChange(e.target.value, detail.id)} type="text" id={detail.detailName} name={detail.detailName} value={detail.value} placeholder={`${detail.detailName}...`}/>
                    </div>)}
                    <label htmlFor="inputData">Upload images: </label>
                    <input type="file" id="inputData" name="inputData" className="inputData" accept="image/*" multiple onChange={e => onImageUpload(e)}/>
                </form>
            </div>
        }
        </>
    )
}

ProductDetailsForm.propTypes = {
    details: Proptypes.array,
    onDetailsChange: Proptypes.func,
    subCategoryId: Proptypes.number,
    user: Proptypes.object,
    onDetailsSet: Proptypes.func,
    onImageUpload: Proptypes.func
}

export default ProductDetailsForm;