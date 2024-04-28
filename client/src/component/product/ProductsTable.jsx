import { Link } from "react-router-dom";
import Proptypes from 'prop-types';

import calculateImageSourceName from "../../utility/calculateImageSourceName";

function ProductsTable({products, buttons}){


    return(
        <table className="products-table">
            <thead>
                <tr>
                    {Object.keys(products[0]).map(fieldName => fieldName !== 'id' ? <th key={fieldName}>{fieldName}</th> : '')}
                    {buttons.map(button => <th key={button.actionName}>{button.actionName}</th>)}
                </tr>
            </thead>
            <tbody>
                {products.map(product => {
                    return (
                        <tr key={product.id}>
                            {Object.keys(product).map(fieldName => fieldName !== 'id' ? <td key={`${fieldName}-${product.id}`}> {fieldName.toLocaleLowerCase().includes('image') ? <img className="my-product-image" src={calculateImageSourceName(product[fieldName])}/> : product[fieldName]}</td> : '')}
                            {buttons.map(button => (
                                <td key={`${button.actionName}-${product.id}`}>
                                    <Link to={button.isDynamic && button.linkTo ? `${button.linkTo}/${product.id}` : button.linkTo}>
                                        <button onClick={button.onClick ? button.isDynamic ? () => button.onClick(product.id) : () => button.onClick() : () => ''}>{button.buttonText}</button>
                                    </Link>
                                </td>
                            ))}
                        </tr>
                    )
                })}
            </tbody>
        </table>
    )
}

ProductsTable.propTypes = {
    products: Proptypes.array,
    buttons: Proptypes.array
}

export default ProductsTable;