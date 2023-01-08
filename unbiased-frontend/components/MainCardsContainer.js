import types from "../types";

export default function MainCardsContainer(props) {
    return <div className="container-fluid">
        <div className={`row row-cols-md-${props.rows}`}>
            {props.cards}
        </div>

    </div>
}

MainCardsContainer.propTypes = types.MainCardsContainer