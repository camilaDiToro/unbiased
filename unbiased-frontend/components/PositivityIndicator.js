import {useAppContext} from "../context";
import PropTypes from 'prop-types';

export default function PositivityIndicator(props) {
    const ctx = useAppContext()
    const tooltipText = `${ctx.I18n("home.upvotes", [props.upvoted])} - ${ctx.I18n("home.interactions", [props.interactions])}`
    const imgMap = {
        positive: "/img/looking-positivity.svg",
        controversial: "/img/controversial-positivity.svg",
        negative: "/img/angry-positivity.svg"
    }





    return <img src={imgMap[props.positivity || 'positive']} alt="..."
                className={`quality-indicator${props.showNews ? '-news-view' : ''}`} data-toggle="tooltip" data-placement="top" title={tooltipText}
                data-original-title={tooltipText} />
}

PositivityIndicator.propTypes = {
    positivity: PropTypes.string,
    upvoted: PropTypes.number,
    interactions: PropTypes.number.isRequired,
}