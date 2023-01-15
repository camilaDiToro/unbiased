import Link from "next/link";

export default function ProfileLink(props) {
    return <Link className="link" href={`/profile/${props.id}`}>
        <div className={` link-text card-name-text text-ellipsis-1 ${props.bold ? 'font-weight-bold' : ''}`}>{props.nameOrEmail}</div>
    </Link>
}