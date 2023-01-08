import Link from "next/link";
import PositivityIndicator from "./PositivityIndicator";
import PropTypes from "prop-types";
import types from "../types";

export default function Creator(props) {
return <>
    <div className="col mb-4">
        <Link className="link" href="/">

            <div className="card h-100 d-flex flex-row">
                {props.isJournalist ? <PositivityIndicator {...props.stats}></PositivityIndicator> : <></>}
                <div className="d-flex justify-content-between p-2 w-100">
                    <div className="d-flex align-items-center w-auto gap-1">
                        <div className="img-container-article">
                            <img className="rounded-circle object-fit-cover mr-1"
                                 src={props.hasImage ? `/profile/${props.id}/image` : "/img/profile-image.png"} alt=""/>
                        </div>
                        <div className="link-text card-name-text text-ellipsis-1">{props.nameOrEmail}</div>

                    </div>
                </div>


            </div>
        </Link>

    </div>
</>
}

Creator.propTypes = types.Creator