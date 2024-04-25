import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import fetchGet from "../utility/fetchGet";

function Item(){
    const [product, setProduct] = useState();

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
                setProduct(productResult);
            } else{
                console.log(productResult);
            }
        }

        getProduct();
        
    }, [id, navigate])

    return(
        <div>
            <h1>Product: {id}</h1>
        </div>
    )
}

export default Item;