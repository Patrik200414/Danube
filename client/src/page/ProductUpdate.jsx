import { useNavigate, useParams } from "react-router-dom";
import useFetchGetAuthorization from "../utility/customHook/useFetchGetAuthorization";
import useVerifyUserRole from "../utility/customHook/useVerifyUserRole";
import ProductInformationForm from "../component/product/ProductInformationForm";
import ProductDetailsForm from "../component/product/ProductDetailsForm";
import changeProductDetail from '../utility/changeProductDetail';
import UploadedImages from '../component/product/UploadedImages';
import imageUpload from "../utility/imageUpload";
import appendFilesToFormData from "../utility/appendFilesToFormData";
import { useState } from "react";
import useVerifyUserAccess from "../utility/customHook/useVerifyUserAccess";

function ProductUpdate(){
    const {productId} = useParams();

    const isAccessible = useVerifyUserAccess('/verification/product/update');
    const [user] = useVerifyUserRole('ROLE_SELLER');
    
    const [product, setProduct, productError] = useFetchGetAuthorization(`/api/product/update/item/${productId}`, user);
    const [error, setError] = useState();
    const navigate = useNavigate();


    function handleProductInformationChange(value, key){
        const updatedProduct = {
            ...product
        };
        updatedProduct.productInformation[key] = value;
        setProduct(updatedProduct);
    }

    function handleDetailChange(value, detailId){
        const copiedProductDetails = [...product.detailValues];
        const updatedDetails = changeProductDetail(value, detailId, copiedProductDetails);
        const updatedProduct = {
            ...product,
            detailValues: [...updatedDetails]
        }

        setProduct(updatedProduct);
    }

    function handleImageUpload(e){
        const uploadedFiles = e.target.files;
        const file = imageUpload(uploadedFiles, product.images);
        const modifiedImages = [...product.images, ...file];
        setProduct(prev => ({
            ...prev,
            images: modifiedImages
        }));
    }

    function handleImageDeletion(index){
        const newImageList = product.images.filter((image, i) => {
            if(i !== index){
                return image;
            }
        })

        setProduct(prev => ({
            ...prev,
            images: newImageList
        }));
    }


    async function handleSubmit(e){
        e.preventDefault();
        const productCopy = {...product};

        const newImages = productCopy.images.filter(image => image instanceof File) ;
        productCopy.images = productCopy.images.filter(image => !(image instanceof File));


        const formData = appendFilesToFormData('newImages', newImages);
        formData.append('updatedValues', JSON.stringify(productCopy));
        formData.append('seller', user.id);
        
        
        const productUpdateData = await fetch(`/api/product/update/${productId}`, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${user.jwt}`
            },
            body: formData
        })

        if(productUpdateData.ok){
            navigate('/product/update');
        } else{
            const errorResponse = await productUpdateData.json();
            setError(errorResponse.errorMessage);
        }
    }

    
    return(
        <div className="product-update-container">
            {(isAccessible && product) ?
                <div className="product-upload">
                    <ProductInformationForm onDetailsChange={(value, key) => handleProductInformationChange(value, key)} productDetail={product.productInformation}/>
                    <ProductDetailsForm details={product.detailValues} onDetailsChange={(value, key) => handleDetailChange(value, key)} subCategoryId={product.productInformation.subcategoryId} user={user} onImageUpload={(e) => handleImageUpload(e)}/>
                    <UploadedImages onImageDeletion={(index) => handleImageDeletion(index)} images={product.images}/>
                    <p className="error-message">{error}</p>
                    <button onClick={handleSubmit} className="submit-button">Save</button>
                </div>
                :
                <h1 className="item-not-found-error">{productError}</h1>
            }
        </div>
    )
}

export default ProductUpdate;