import { useParams } from "react-router-dom";
import useFetch from "../utility/customHook/useFetch";
import useVerifySeller from "../utility/customHook/useVerifySeller";
import ProductInformationForm from "../component/product/ProductInformationForm";
import ProductDetailsForm from "../component/product/ProductDetailsForm";
import changeProductDetail from '../utility/changeProductDetail';

function ProductUpdate(){
    const {productId} = useParams();

    const [user] = useVerifySeller();
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



    
    return(
        <div className="product-update-container">
            {product &&
                <div className="product-upload">
                    <ProductInformationForm onDetailsChange={(value, key) => handleProductInformationChange(value, key)} productDetail={product.productInformation}/>
                    <ProductDetailsForm details={product.detailValues} onDetailsChange={(value, key) => handleDetailChange(value, key)} subCategoryId={product.productInformation.subcategoryId} user={user} onDetailsSet={() => console.log('asd')} onImageUpload={() => console.log('asd')}/>
                    <button type="submit">Save</button>
                </div>
            }
        </div>
    )
}

export default ProductUpdate;