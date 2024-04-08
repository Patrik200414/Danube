import { useEffect, useState } from "react";
import ProductCard from "./ProductCard";

function ProductContainer(){
    const [products, setProducts] = useState();
    const [productCount, setProductCount] = useState(10);

    const [error, setError] = useState();

    useEffect(() => {
        const getProducts = async () => {
            const productsData = await fetch(`/api/product`);
            const productResponse = await productsData.json();
            console.log(productResponse);
            if(productsData.ok){
                setProducts(productResponse);
                setError();
            } else{
                setError(productResponse.errorMessage);
            }
        }

        getProducts();
    }, [productCount])

    return(
        <div className="product-container">
            {error && <p className="error-message">{error}</p>}
            {products && products.map(product => <ProductCard key={product.id} product={product}/>)}
        </div>
    )
}

export default ProductContainer;