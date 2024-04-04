import Proptypes from "prop-types";
import { useEffect } from "react";

function ProductDetailsForm({details, onDetailsChange, subCategoryId, user, onDetailsSet, onImageUpload}){

    useEffect(() => {
        const getDetails = async () => {
            const detailsData = await fetch(`/api/product/detail/${subCategoryId}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${user.jwt}`
                }
            });
            const detailResponse = await detailsData.json();
            onDetailsSet(detailResponse);
        }
        getDetails();
    }, [subCategoryId])


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
                    <input type="file" id="inputData" name="inputData" className="inputData" multiple onChange={e => onImageUpload(e)}/>
                </form>
            </div>
        }
        </>
    )
}

ProductDetailsForm.propTypes = {
    details: Proptypes.array,
    onDetailsChange: Proptypes.func,
    subCategoryId: Proptypes.string,
    user: Proptypes.object,
    onDetailsSet: Proptypes.func,
    onImageUpload: Proptypes.func
}

export default ProductDetailsForm;