import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import fetchGet from "../utility/fetchGet";
import ItemImageViewer from "../component/item/ItemImageViewer";
import ItemTableRow from '../component/item/ItemTableRow';
import ItemTableContainer from '../component/item/ItemTableContainer';


function Item(){
    const [product, setProduct] = useState();
    const [error, setError] = useState();

    const {id} = useParams();
    const navigate = useNavigate();

    function renderProductDetails(detailValues){
        return detailValues.map(detailValue => <ItemTableRow key={detailValue.detail} productPropertyName={detailValue.detail} productPropertyValue={detailValue.value}/>);
    }

    function renderProductInformation(productInformation){
        return Object.keys(productInformation).map(productInformationKey => <ItemTableRow key={productInformationKey} productPropertyName={productInformationKey} productPropertyValue={productInformation[productInformationKey]}/>);
    }

    useEffect(() => {
        if(isNaN(id)){
            navigate('/');
            return;
        }

        const getProduct = async () => {
            const searchedProduct = await fetchGet(`/api/product/item/${id}`);
            const productResult = await searchedProduct.json();
            if(searchedProduct.ok){
                setProduct(productResult);
            }
            setError(!searchedProduct.ok ? productResult.errorMessage : '');
        }

        getProduct();
        
    }, [id, navigate])


    return(
        <div className="product-info-container">
            {error ? 
            <h1 className="item-not-found-error">{error}</h1> : 
            product &&
            <div className="item-container">
                <ItemImageViewer images={product.images}/>
                <div className="item-information-container">
                    <ItemTableContainer tableName="Product Information" renderElement={() => renderProductInformation(product.productInformation)}/>
                    <ItemTableContainer tableName="Product Details" renderElement={() => renderProductDetails(product.detailValues)}/>
                </div>
            </div>
            }
        </div>
    )
}

export default Item;