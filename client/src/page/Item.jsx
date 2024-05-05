import PropTypes from 'prop-types';
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import fetchGet from "../utility/fetchGet";
import ItemImageViewer from "../component/item/ItemImageViewer";
import ItemTableRow from '../component/item/ItemTableRow';
import ItemTableContainer from '../component/item/ItemTableContainer';
import ItemSimilarContainer from "../component/item/ItemSimilarContainer";
import handlePromiseAllError from "../utility/handlePromiseAllError";
import AddToCart from "../component/order/AddToCart";


function Item({onNavbarInformationChange}){
    const [product, setProduct] = useState();
    const [simularProducts, setSimularProducts] = useState();
    const [errors, setErrors] = useState([]);


    const {id} = useParams();
    const navigate = useNavigate();

    function renderProductDetails(detailValues){
        return detailValues.map(detailValue => <ItemTableRow key={detailValue.id} productPropertyName={detailValue.detailName} productPropertyValue={detailValue.value}/>);
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
            const itemsAndSimilarItems = await Promise.all([
                fetchGet(`/api/product/item/${id}`),
                fetchGet(`/api/product/similar/${id}`)
            ]);

            const errorHandlingResponse = await handlePromiseAllError(itemsAndSimilarItems, (resultErrors) => setErrors(resultErrors));
            if(errorHandlingResponse){
                setProduct(await itemsAndSimilarItems[0].json());
                setSimularProducts(await itemsAndSimilarItems[1].json());
            }
        }

        getProduct();
        
    }, [id, navigate])


    return(
        <div className="product-info-container">
            {errors.length ? 
            <div className="multiple-error-container">
                {errors.map(error => <h1 key={error} className="item-not-found-error">{error}</h1>)}
            </div> : 
            (product && simularProducts) &&
            <div>
                <div className="item-container">
                    <ItemImageViewer images={product.images}/>
                    <div className="item-information-container">
                        <ItemTableContainer tableName="Product Information" renderElement={() => renderProductInformation(product.productInformation)}/>
                        <ItemTableContainer tableName="Product Details" renderElement={() => renderProductDetails(product.detailValues)}/>
                    </div>
                </div>
                <AddToCart maxQuantity={product.productInformation.quantity} productId={Number(id)} onNavbarInformationChange={(information) => onNavbarInformationChange(information)}/>
                <ItemSimilarContainer similarProducts={simularProducts}/>
            </div>
            }
        </div>
    )
}

Item.propTypes = {
    onNavbarInformationChange: PropTypes.func
}

export default Item;