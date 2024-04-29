import { useParams } from "react-router-dom";
import useFetch from "../utility/customHook/useFetch";
import useVerifySeller from "../utility/customHook/useVerifySeller";
import ProductInformationForm from "../component/product/ProductInformationForm";

function ProductUpdate(){
    const {productId} = useParams();

    const [user] = useVerifySeller();
    const [product] = useFetch(`/api/product/item/${productId}`);

    return(
        <div className="product-update-container">
            {product &&
                <ProductInformationForm onDetailsChange={() => console.log('asd')} productDetail={product.productInformation}/>
            }
        </div>
    )
}

export default ProductUpdate;