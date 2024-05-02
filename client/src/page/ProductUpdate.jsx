import { useParams } from "react-router-dom";
import useFetchGetAuthorization from "../utility/customHook/useFetchGetAuthorization";
import useVerifyUser from "../utility/customHook/useVerifyUser";
import ProductInformationForm from "../component/product/ProductInformationForm";
import ProductDetailsForm from "../component/product/ProductDetailsForm";
import changeProductDetail from '../utility/changeProductDetail';
import UploadedImages from '../component/product/UploadedImages';
import imageUpload from "../utility/imageUpload";
import appendFilesToFormData from "../utility/appendFilesToFormData";

function ProductUpdate(){
    const {productId} = useParams();

    const [user] = useVerifyUser('ROLE_SELLER');
    
    const [product, setProduct] = useFetchGetAuthorization(`/api/product/update/item/${productId}`, user);


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
        const newImages = [];
        
        for(let i = 0; i < productCopy.images.length; i++){
            if(typeof productCopy.images[i] === 'object'){
                const newImage = productCopy.images.splice(i, 1)[0];
                newImages.push(newImage);
            }
        }

        const formData = appendFilesToFormData(new FormData(), 'newImages', newImages);
        formData.append('updatedValues', JSON.stringify(productCopy));
        formData.append('seller', user.id);
        
        console.log(newImages);
        const productUpdateData = await fetch(`/api/product/update/${productId}`, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${user.jwt}`
            },
            body: formData
        })
        /* const productUpdateData = fetchPostAuthorizationFetch(`/api/product/update/${productId}`, user.jwt, formData); */
    }

    
    return(
        <div className="product-update-container">
            {product &&
                <div className="product-upload">
                    <ProductInformationForm onDetailsChange={(value, key) => handleProductInformationChange(value, key)} productDetail={product.productInformation}/>
                    <ProductDetailsForm details={product.detailValues} onDetailsChange={(value, key) => handleDetailChange(value, key)} subCategoryId={product.productInformation.subcategoryId} user={user} onImageUpload={(e) => handleImageUpload(e)}/>
                    <UploadedImages onImageDeletion={(index) => handleImageDeletion(index)} images={product.images.map(image => image)}/>
                    <button onClick={handleSubmit} className="submit-button">Save</button>
                </div>
            }
        </div>
    )
}

export default ProductUpdate;