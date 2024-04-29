import { useParams } from "react-router-dom";
import useFetch from "../utility/customHook/useFetch";
import useVerifySeller from "../utility/customHook/useVerifySeller";
import ProductInformationForm from "../component/product/ProductInformationForm";
import ProductDetailsForm from "../component/product/ProductDetailsForm";

function ProductUpdate(){
    const {productId} = useParams();

    const [user] = useVerifySeller();
    const [product] = useFetch(`/api/product/item/${productId}`);

    
    return(
        <div className="product-update-container">
            {product &&
                <div>
                    <ProductInformationForm onDetailsChange={() => console.log('asd')} productDetail={product.productInformation}/>
                    <ProductDetailsForm details={product.detailValues} onDetailsChange={() => console.log('asd')} subCategoryId={1} user={user} onDetailsSet={() => console.log('asd')} onImageUpload={() => console.log('asd')}/>
                </div>
            }
        </div>
    )
}

export default ProductUpdate;