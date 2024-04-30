import { useParams } from "react-router-dom";
import useFetch from "../utility/customHook/useFetch";
import useVerifyUser from "../utility/customHook/useVerifyUser";
import ProductInformationForm from "../component/product/ProductInformationForm";
import ProductDetailsForm from "../component/product/ProductDetailsForm";
import changeProductDetail from '../utility/changeProductDetail';
import UploadedImages from '../component/product/UploadedImages';
import imageUpload from "../utility/imageUpload";

function ProductUpdate(){
    const {productId} = useParams();

    const [user] = useVerifyUser('ROLE_SELLER');
    const [product, setProduct] = useFetch(`/api/product/item/${productId}`);


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


    
    return(
        <div className="product-update-container">
            {product &&
                <div className="product-upload">
                    <ProductInformationForm onDetailsChange={(value, key) => handleProductInformationChange(value, key)} productDetail={product.productInformation}/>
                    <ProductDetailsForm details={product.detailValues} onDetailsChange={(value, key) => handleDetailChange(value, key)} subCategoryId={product.productInformation.subcategoryId} user={user} onImageUpload={(e) => handleImageUpload(e)}/>
                    <UploadedImages onImageDeletion={(index) => handleImageDeletion(index)} images={product.images.map(image => image)}/>
                    <button type="submit">Save</button>
                </div>
            }
        </div>
    )
}

export default ProductUpdate;