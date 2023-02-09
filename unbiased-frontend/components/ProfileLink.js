import Link from "next/link";
import types from "../types";

export default function ProfileLink(props) {
    return <Link className="link" href={`/profile/${props.id}`}>
        <div className={` link-text card-name-text ${props.bold ? 'font-weight-bold' : ''} ${props.shorten ? '': 'w-fit' }`}>{props.nameOrEmail}</div>
    </Link>
}

ProfileLink.propTypes = types.ProfileLink