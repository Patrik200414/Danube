import ProductCard from "./ProductCard";
import Proptypes from "prop-types";

function ProductContainer({products}){
    return(
        <div className="product-container">
            {products && products.map(product => <ProductCard key={product.id} product={product}/>)}
        </div>
    )
}

ProductContainer.propTypes = {
    products: Proptypes.array
}

export default ProductContainer;