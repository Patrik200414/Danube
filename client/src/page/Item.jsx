import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import fetchGet from "../utility/fetchGet";
import ItemImageViewer from "../component/item/ItemImageViewer";
import ItemTable from "../component/item/ItemTable";

function Item(){
    const [product, setProduct] = useState();
    const [error, setError] = useState();

    const {id} = useParams();
    const navigate = useNavigate();

    useEffect(() => {
        if(isNaN(id)){
            navigate('/');
            return;
        }

        const getProduct = async () => {
            const searchedProduct = await fetchGet(`/api/product/item/${id}`);
            const productResult = await searchedProduct.json();
            if(searchedProduct.ok){
                console.log(productResult);
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
            <div>
                <ItemImageViewer images={product.images}/>
                <ItemTable detailValues={product.detailValues}/>
            </div>
            }
        </div>
    )
}

export default Item;